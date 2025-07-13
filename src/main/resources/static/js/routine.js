document.addEventListener('DOMContentLoaded', () => {
  // 新規ルーティンボタン
  document.querySelectorAll('#new-routine-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      fetch('/routine-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: '', type: '予定', frequency: '毎日' }),
      }).then(() => location.reload());
    });
  });

  // 削除ボタン
  document.querySelectorAll('.routine-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/routine-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) }),
      }).then(() => location.reload());
    });
  });

  // 入力変更
  document
    .querySelectorAll('.routine-name-input, .routine-type-select, .routine-frequency-select')
    .forEach((el) => {
      el.addEventListener('change', () => {
        const row = el.closest('tr');
        if (row) sendUpdate(row);
      });
    });

  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      name: row.querySelector('.routine-name-input').value,
      type: row.querySelector('.routine-type-select').value,
      frequency: row.querySelector('.routine-frequency-select').value,
    };
  }

  function sendUpdate(row) {
    const data = gatherData(row);
    return fetch('/routine-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
  }
});
