document.addEventListener('DOMContentLoaded', () => {
  // 新規日記ボタン
  document.querySelectorAll('#new-diary-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      fetch('/diary-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ recordDate: new Date().toISOString().split('T')[0], content: '' })
      }).then(() => location.reload());
    });
  });

  // 削除ボタン
  document.querySelectorAll('.diary-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/diary-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) })
      }).then(() => location.reload());
    });
  });

  // 入力変更
  document
    .querySelectorAll('.diary-date-input, .diary-content-input')
    .forEach((el) => {
      el.addEventListener('change', () => {
        const row = el.closest('tr');
        if (row) sendUpdate(row);
      });
    });

  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      recordDate: row.querySelector('.diary-date-input').value,
      content: row.querySelector('.diary-content-input').value
    };
  }

  function sendUpdate(row) {
    const data = gatherData(row);
    fetch('/diary-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
  }
});
