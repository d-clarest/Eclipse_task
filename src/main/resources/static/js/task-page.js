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

  function save() {
    fetch('/task-page-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: pageId, content: textarea.value })
    });
  }
});
