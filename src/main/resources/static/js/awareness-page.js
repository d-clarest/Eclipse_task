document.addEventListener('DOMContentLoaded', () => {
  const textarea = document.getElementById('awareness-page-content');
  const awarenessInput = document.getElementById('awareness-input');

  if (textarea) {
    autoResize();
    textarea.addEventListener('input', autoResize);
    textarea.addEventListener('change', savePage);
  }

  if (awarenessInput) {
    enableFullTextDisplay();
    awarenessInput.addEventListener('change', saveRecord);
  }

  function autoResize() {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
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

  function enableFullTextDisplay() {
    let originalWidth = '';
    const adjustWidth = () => {
      awarenessInput.style.width = 'auto';
      const w = awarenessInput.scrollWidth + 4;
      awarenessInput.style.width = w + 'px';
    };
    adjustWidth();
    awarenessInput.addEventListener('focus', () => {
      originalWidth = awarenessInput.style.width || awarenessInput.offsetWidth + 'px';
      adjustWidth();
    });
    awarenessInput.addEventListener('input', adjustWidth);
    awarenessInput.addEventListener('blur', () => {
      awarenessInput.style.width = originalWidth;
    });
  }
});
