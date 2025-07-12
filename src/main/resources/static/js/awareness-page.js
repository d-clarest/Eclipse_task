document.addEventListener('DOMContentLoaded', () => {
  const textarea = document.getElementById('awareness-page-content');
  textarea.addEventListener('change', save);//テクストエリアに記入が完了したら

  if (!textarea) return;
  //AwarenessPageControllerにPOST
  const save = () => {
    fetch('/awareness-page-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: pageId, content: textarea.value })
    });
  };
});
