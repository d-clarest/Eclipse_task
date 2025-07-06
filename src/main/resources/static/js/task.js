document.addEventListener('DOMContentLoaded', () => {
  const newButton = document.getElementById('new-task-button');
  if (newButton) {
    newButton.addEventListener('click', () => {
      fetch('/task-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: '', result: '', detail: '', level: 1 })
      }).then(() => location.reload());
    });
  }

  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      title: row.querySelector('.task-title-input').value,
      result: row.querySelector('.task-result-input').value,
      detail: row.querySelector('.task-detail-input').value,
      level: parseInt(row.querySelector('.task-level-select').value, 10)
    };
  }

  function sendUpdate(row) {
    const data = gatherData(row);
    fetch('/task-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
  }

  ['.task-title-input', '.task-result-input', '.task-detail-input', '.task-level-select'].forEach((selector) => {
    document.querySelectorAll(selector).forEach((inp) => {
      const handler = () => {
        const row = inp.closest('tr');
        if (row) sendUpdate(row);
      };
      inp.addEventListener('change', handler);
      inp.addEventListener('input', handler);
    });
  });
});
