document.addEventListener('DOMContentLoaded', () => {
  const textarea = document.getElementById('task-page-content');
  textarea.addEventListener('change', save);//テクストエリアに記入が完了したら

  if (!textarea) return;
  const save = () => {
    //TaskPageControllerにPOST
    fetch('/task-page-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: pageId, content: textarea.value })
    });
  };
});
