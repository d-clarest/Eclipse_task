//ページが全部読み込まれたら、中の処理を始める
document.addEventListener('DOMContentLoaded', () => {
  const newButton = document.getElementById('new-challenge-button');
  if (newButton) {
    newButton.addEventListener('click', () => {
      fetch('/challenge-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: '' })
      }).then(() => {
        location.reload();
      });
    });
  }

  const unchallengedTable = document.getElementById('unchallenged-table');
  const completedTable = document.getElementById('completed-challenge-table');

  function moveRow(row, completed) {
    const target = completed ? completedTable : unchallengedTable;
    if (!row || !target) return;
    const tbody = target.tBodies[0] || target;
    tbody.appendChild(row);
  }

  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      title: row.querySelector('.challenge-title-input').value,
      risk: row.querySelector('.challenge-risk-input').value,
      expectedResult: row.querySelector('.challenge-expected-input').value,
      strategy: row.querySelector('.challenge-strategy-input').value,
      actualResult: row.querySelector('.challenge-actual-input').value,
      improvementPlan: row.querySelector('.challenge-improvement-input').value,
      challengeLevel: parseInt(row.querySelector('.challenge-level-input').value, 10),
      challengeDate: row.querySelector('.challenge-date-input').value || null
    };
  }

  function sendUpdate(row) {
    const data = gatherData(row);
    fetch('/challenge-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
  }

  document.querySelectorAll('.challenge-suc-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const input = row.querySelector('.challenge-actual-input');
      if (input) input.value = '成功';
      sendUpdate(row);
      moveRow(row, true);
    });
  });

  document.querySelectorAll('.challenge-fail-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const input = row.querySelector('.challenge-actual-input');
      if (input) input.value = '失敗';
      sendUpdate(row);
      moveRow(row, true);
    });
  });

  document.querySelectorAll('.challenge-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/challenge-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) })
      }).then(() => {
        row.remove();
      });
    });
  });
});
