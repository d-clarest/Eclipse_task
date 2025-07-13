document.addEventListener('DOMContentLoaded', () => {
  const el = document.getElementById('current-time');
  if (!el) return;
  const update = () => {
    const now = new Date();
    const h = String(now.getHours()).padStart(2, '0');
    const m = String(now.getMinutes()).padStart(2, '0');
    const s = String(now.getSeconds()).padStart(2, '0');
    el.textContent = `${h}時${m}分${s}秒`;
  };
  update();
  setInterval(update, 1000);
});
