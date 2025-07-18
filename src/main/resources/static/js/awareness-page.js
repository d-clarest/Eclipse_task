document.addEventListener('DOMContentLoaded', () => {
  const textarea = document.getElementById('awareness-page-content');
  const awarenessInput = document.getElementById('awareness-input');

  if (textarea) {
    textarea.addEventListener('change', savePage);
  }

  if (awarenessInput) {
    awarenessInput.addEventListener('change', saveRecord);
  }

  function savePage() {
    fetch('/awareness-page-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: pageId, content: textarea.value })
    });
  }

  function saveRecord() {
    fetch('/awareness-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        id: recordId,
        awareness: awarenessInput.value,
        awarenessLevel: awarenessLevel
      })
    });
  }
});
