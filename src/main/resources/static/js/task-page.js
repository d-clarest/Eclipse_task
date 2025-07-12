document.addEventListener('DOMContentLoaded', () => {
  const textarea = document.getElementById('task-page-content');
  if (textarea) {
    textarea.addEventListener('change', save);
  }

  const newButton = document.getElementById('new-subtask-button');
  if (newButton) {
    newButton.addEventListener('click', () => {
      fetch('/subtask-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ taskId: taskId, title: '' }),
        keepalive: true,
      }).then(() => location.reload());
    });
  }

  document.querySelectorAll('.subtask-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/subtask-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) }),
        keepalive: true,
      }).then(() => location.reload());
    });
  });

  document.querySelectorAll('.subtask-complete-button').forEach((btn) => {
    const row = btn.closest('tr');
    const comp = row ? row.querySelector('.subtask-completed-input') : null;
    if (comp && comp.value) {
      btn.value = '取消';
    }
    btn.addEventListener('click', () => {
      if (!row || !comp) return;
      if (btn.value === '完了') {
        comp.value = new Date().toISOString().split('T')[0];
        btn.value = '取消';
      } else {
        comp.value = '';
        btn.value = '完了';
      }
      sendUpdate(row).then(() => location.reload());
    });
  });

  document
    .querySelectorAll('.subtask-title-input, .subtask-completed-input')
    .forEach((inp) => {
      inp.addEventListener('change', () => {
        const row = inp.closest('tr');
        if (row) sendUpdate(row);
      });
    });

  function save() {
    fetch('/task-page-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: pageId, content: textarea.value })
    });
  }

  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      title: row.querySelector('.subtask-title-input').value,
      deadline: row.dataset.deadline || null,
      completedAt: row.querySelector('.subtask-completed-input').value || null,
      taskId: taskId,
    };
  }

  function sendUpdate(row) {
    const data = gatherData(row);
    return fetch('/subtask-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
      keepalive: true,
    });
  }
});
