//ページが全部読み込まれたら、中の処理を始める
document.addEventListener('DOMContentLoaded', () => {
  const newButton = document.getElementById('new-challenge-button');
  if (newButton) {
    newButton.addEventListener('click', () => {
      fetch('/challenge-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: '' })
      }).then(() => {
        location.reload();
      });
    });
  }
});
