document.addEventListener('DOMContentLoaded', () => {
  const overlay = document.getElementById('schedule-overlay');
  const popup = document.getElementById('schedule-popup');
  const closeBtn = document.getElementById('close-schedule-popup');
  const listEl = document.getElementById('schedule-popup-list');
  const titleEl = document.getElementById('schedule-popup-title');

  function getYearMonth() {
    const title = document.getElementById('calendar-title');
    if (title) {
      const m = title.textContent.match(/(\d+)年(\d+)月/);
      if (m) {
        return { year: parseInt(m[1], 10), month: parseInt(m[2], 10) };
      }
    }
    const now = new Date();
    return { year: now.getFullYear(), month: now.getMonth() + 1 };
  }

  function hide() {
    overlay.style.display = 'none';
    popup.style.display = 'none';
  }

    function show(dateStr) {
      listEl.innerHTML = '';
      const rows = document.querySelectorAll('.schedule-row');
      let items = [];
      const seen = new Set();
      rows.forEach((row) => {
        const schedDate = row.querySelector('.schedule-date-input').value;
        if (schedDate !== dateStr) return;
        const addFlag = row.querySelector('.schedule-add-flag');
        const completed = !!row.querySelector('.completed-day-input')?.value;
        if (!completed && (!addFlag || !addFlag.checked)) return;
        const title = row.querySelector('.schedule-title-input').value;
        const start =
          row.querySelector('.start-hour').value.padStart(2, '0') +
          ':' +
          row.querySelector('.start-minute').value.padStart(2, '0');
        const end =
          row.querySelector('.end-hour').value.padStart(2, '0') +
          ':' +
          row.querySelector('.end-minute').value.padStart(2, '0');
        const id = row.dataset.id;
        items.push({ start, end, title, completed, id });
        if (id) seen.add(id);
      });
      document
        .querySelectorAll('#calendar .schedules div')
        .forEach((div) => {
          if (div.dataset.date !== dateStr) return;
          const id = div.dataset.id;
          if (id && seen.has(id)) return;
          const start = (div.dataset.start || '').slice(0, 5);
          const end = (div.dataset.end || '').slice(0, 5);
          const title = div.dataset.name || div.textContent;
          const completed =
            div.dataset.completed === 'true' || div.style.color === 'red';
          items.push({ start, end, title, completed });
        });
      items.sort((a, b) => a.start.localeCompare(b.start));
      if (items.length === 0) {
        const li = document.createElement('li');
        li.textContent = '予定はありません';
        listEl.appendChild(li);
      } else {
        items.forEach((it) => {
          const li = document.createElement('li');
          li.textContent = `${it.start} - ${it.end} ${it.title}`;
          if (it.completed) {
            li.style.color = 'red';
          }
          listEl.appendChild(li);
        });
      }
      titleEl.textContent = dateStr;
      overlay.style.display = 'block';
      popup.style.display = 'block';
    }

  closeBtn.addEventListener('click', hide);
  overlay.addEventListener('click', hide);

  function attach() {
    const table = document.getElementById('calendar');
    if (!table) return;
    table.addEventListener('click', (e) => {
      const td = e.target.closest('td');
      if (!td || !td.parentElement) return;
      const day = parseInt(td.textContent, 10);
      if (isNaN(day)) return;
      const { year, month } = getYearMonth();
      const dateStr = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
      show(dateStr);
    });
  }

  attach();
  document.addEventListener('calendarRendered', () => {
    // no need to reattach since using event delegation
  });
});
