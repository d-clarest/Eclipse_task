document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('#new-word-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      fetch('/word-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ word: '', meaning: '', example: '' })
      }).then(() => location.reload());
    });
  });

  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      word: row.querySelector('.word-input').value,
      meaning: row.querySelector('.meaning-input').value,
      example: row.querySelector('.example-input').value
    };
  }

  function sendUpdate(row) {
    const data = gatherData(row);
    return fetch('/word-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
  }

  document.querySelectorAll('.word-input, .meaning-input, .example-input').forEach((el) => {
    el.addEventListener('input', () => {
      const row = el.closest('tr');
      if (row) sendUpdate(row);
    });
  });

  document.querySelectorAll('.word-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/word-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) })
      }).then(() => location.reload());
    });
  });
});
