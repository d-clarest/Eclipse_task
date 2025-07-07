//ページが全部読み込まれたら、中の処理を始める
document.addEventListener('DOMContentLoaded', () => {
  const newButton = document.getElementById('new-challenge-button');
  if (newButton) {
    newButton.addEventListener('click', () => {
      fetch('/challenge-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: '' }),
      }).then(() => {
        location.reload();
      });
    });
  }

  const unchallengedTable = document.getElementById('unchallenged-table');
  const completedTable = document.getElementById('completed-challenge-table');
  const pointDisplay = document.getElementById('total-point-display');

  function refreshTotalPoint() {
    if (!pointDisplay) return;
    fetch('/total-point')
      .then((res) => res.json())
      .then((pt) => {
        pointDisplay.textContent = `${pt}P`;
      });
  }

  function moveRow(row, toCompleted) {
    if (!row) return;
    const target = toCompleted ? completedTable : unchallengedTable;
    if (target) {
      const tbody = target.tBodies[0] || target;
      tbody.appendChild(row);
    } else {
      row.style.display = toCompleted ? 'none' : '';
    }
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
      challengeDate: row.querySelector('.challenge-date-input').value || null,
    };
  }

  function sendUpdate(row) {
    const data = gatherData(row);
    return fetch('/challenge-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
  }

  function replaceWithCancel(row) {
    const cell = row.querySelector('td:first-child');
    if (!cell) return;
    cell.innerHTML = '<input type="button" value="取消" class="challenge-cancel-button" />';
    const btn = cell.querySelector('.challenge-cancel-button');
    if (btn) addCancelListener(btn);
  }

  function replaceWithSucFail(row) {
    const cell = row.querySelector('td:first-child');
    if (!cell) return;
    cell.innerHTML = '<input type="button" value="成功" class="challenge-suc-button" /> <input type="button" value="失敗" class="challenge-fail-button" />';
    cell.querySelectorAll('.challenge-suc-button').forEach(addSuccessListener);
    cell.querySelectorAll('.challenge-fail-button').forEach(addFailListener);
  }

  function addSuccessListener(btn) {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const actual = row.querySelector('.challenge-actual-input');
      if (actual) actual.value = '（成功）' + actual.value;
      const date = row.querySelector('.challenge-date-input');
      if (date) date.value = new Date().toISOString().split('T')[0];
      sendUpdate(row).then(refreshTotalPoint);
      moveRow(row, true);
      replaceWithCancel(row);
    });
  }

  function addFailListener(btn) {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const actual = row.querySelector('.challenge-actual-input');
      if (actual) actual.value = '（失敗）' + actual.value;
      const date = row.querySelector('.challenge-date-input');
      if (date) date.value = new Date().toISOString().split('T')[0];
      sendUpdate(row).then(refreshTotalPoint);
      moveRow(row, true);
      replaceWithCancel(row);
    });
  }

  function addCancelListener(btn) {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const actual = row.querySelector('.challenge-actual-input');
      if (actual) actual.value = '';
      const date = row.querySelector('.challenge-date-input');
      if (date) date.value = '';
      sendUpdate(row).then(refreshTotalPoint);
      moveRow(row, false);
      replaceWithSucFail(row);
    });
  }

  document.querySelectorAll('.challenge-suc-button').forEach(addSuccessListener);
  document.querySelectorAll('.challenge-fail-button').forEach(addFailListener);
  document.querySelectorAll('.challenge-cancel-button').forEach(addCancelListener);

  document.querySelectorAll('.challenge-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/challenge-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) }),
      }).then(() => {
        row.remove();
      });
    });
  });

  //--------------------------------------------------------------------------------------
  [
    '.challenge-title-input',
    '.challenge-risk-input',
    '.challenge-expected-input',
    '.challenge-strategy-input',
    '.challenge-actual-input',
    '.challenge-improvement-input',
    '.challenge-date-input',
  ].forEach((selector) => {
    document.querySelectorAll(selector).forEach((inp) => {
      const handler = () => {
        const row = inp.closest('tr');
        if (row) sendUpdate(row).then(refreshTotalPoint);

        
      };
      inp.addEventListener('change', handler);
      inp.addEventListener('input', handler);
    });
  });

  function enableFullTextDisplay(selector) {
    document.querySelectorAll(selector).forEach((inp) => {
      let originalWidth = '';
      const adjustWidth = () => {
        inp.style.width = 'auto';
        const w = inp.scrollWidth + 4; // some padding
        inp.style.width = w + 'px';
      };
      inp.addEventListener('focus', () => {
        originalWidth = inp.style.width || inp.offsetWidth + 'px';
        adjustWidth();
      });
      inp.addEventListener('input', adjustWidth);
      inp.addEventListener('blur', () => {
        inp.style.width = originalWidth;
      });
    });
  }

  enableFullTextDisplay('.challenge-title-input, .challenge-risk-input, .challenge-expected-input, .challenge-strategy-input, .challenge-actual-input, .challenge-improvement-input');

  refreshTotalPoint();
});
