document.addEventListener('DOMContentLoaded', () => {
  // 新規夢ボタン
  document.querySelectorAll('#new-dream-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      fetch('/dream-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ dream: '', page: '' })
      }).then(() => location.reload());
    });
  });

  // 削除ボタン
  document.querySelectorAll('.dream-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/dream-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) })
      }).then(() => location.reload());
    });
  });

  // 入力変更
  document.querySelectorAll('.dream-input, .page-input').forEach((el) => {
    el.addEventListener('change', () => {
      const row = el.closest('tr');
      if (row) sendUpdate(row);
    });
  });

  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      dream: row.querySelector('.dream-input').value,
      page: row.querySelector('.page-input').value
    };
  }

  function sendUpdate(row) {
    const data = gatherData(row);
    fetch('/dream-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
  }
});
