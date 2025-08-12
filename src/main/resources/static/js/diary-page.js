document.addEventListener('DOMContentLoaded', () => {
  const textarea = document.getElementById('diary-detail-content');
  const titleInput = document.getElementById('diary-title-input');

  if (textarea) {
    autoResize();
    textarea.addEventListener('input', autoResize);
    textarea.addEventListener('change', save);
  }

  if (titleInput) {
    enableFullTextDisplay();
    titleInput.addEventListener('change', save);
  }

  function autoResize() {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }

  function save() {
    fetch('/diary-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        id: recordId,
        recordDate: recordDate,
        content: titleInput.value,
        detail: textarea.value
      })
    });
  }

  function enableFullTextDisplay() {
    let originalWidth = '';
    const adjustWidth = () => {
      titleInput.style.width = 'auto';
      const w = titleInput.scrollWidth + 4;
      titleInput.style.width = w + 'px';
    };
    adjustWidth();
    titleInput.addEventListener('focus', () => {
      originalWidth = titleInput.style.width || titleInput.offsetWidth + 'px';
      adjustWidth();
    });
    titleInput.addEventListener('input', adjustWidth);
    titleInput.addEventListener('blur', () => {
      titleInput.style.width = originalWidth;
    });
  }
});
