document.addEventListener('DOMContentLoaded', () => {
  const newButton = document.getElementById('new-task-button');
  if (newButton) {
    newButton.addEventListener('click', () => {
      fetch('/task-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: '', category: '今日', result: '', detail: '', level: 1 })
      }).then(() => location.reload());
    });
  }

  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      title: row.querySelector('.task-title-input').value,
      category: row.querySelector('.task-category-select').value,
      result: row.querySelector('.task-result-input').value,
      detail: row.querySelector('.task-detail-input').value,
      level: parseInt(row.querySelector('.task-level-select').value, 10),
      completedAt: row.querySelector('.task-completed-input').value || null
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

  ['.task-title-input', '.task-result-input', '.task-detail-input', '.task-level-select', '.task-completed-input', '.task-category-select'].forEach((selector) => {
    document.querySelectorAll(selector).forEach((inp) => {
      const handler = () => {
        const row = inp.closest('tr');
        if (row) sendUpdate(row);
      };
      inp.addEventListener('change', handler);
      inp.addEventListener('input', handler);
    });
  });

  document.querySelectorAll('.task-complete-button').forEach((btn) => {
    const row = btn.closest('tr');
    const comp = row ? row.querySelector('.task-completed-input') : null;
    if (comp && comp.value) {
      btn.value = '取消';
      if (row) row.style.display = 'none';
    }
    btn.addEventListener('click', () => {
      if (!row || !comp) return;
      if (btn.value === '完了') {
        comp.value = new Date().toISOString().split('T')[0];
        btn.value = '取消';
        row.style.display = 'none';
      } else {
        comp.value = '';
        btn.value = '完了';
        row.style.display = '';
      }
      sendUpdate(row);
    });
  });

  document.querySelectorAll('.task-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/task-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) })
      }).then(() => {
        row.remove();
      });
    });
  });
});
