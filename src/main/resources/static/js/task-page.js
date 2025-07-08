document.addEventListener('DOMContentLoaded', () => {
  const textarea = document.getElementById('task-page-content');
  if (!textarea) return;
  const save = () => {
    fetch('/task-page-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: pageId, content: textarea.value })
    });
  };
  textarea.addEventListener('change', save);
  textarea.addEventListener('input', save);
});
