document.addEventListener('DOMContentLoaded', () => {
  const textarea = document.getElementById('dream-page-content');
  if (!textarea) return;
  const save = () => {
    fetch('/dream-page-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: pageId, content: textarea.value })
    });
  };
  textarea.addEventListener('change', save);
});
