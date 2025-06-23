document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.schedule-date-input').forEach(inp => {
        inp.addEventListener('change', () => {
            const date = new Date(inp.value);
            if (isNaN(date.getTime())) return;
            const dow = date.toLocaleDateString('ja-JP', { weekday: 'short' });
            const row = inp.closest('tr');
            if (row) {
                const dayField = row.querySelector('.schedule-day-of-week');
                if (dayField) {
                    dayField.value = dow;
                }
            }
        });
    });

    function initTimeSelects(hourSel, minuteSel, time) {
        for (let h = 0; h < 24; h++) {
            const opt = document.createElement('option');
            opt.value = String(h).padStart(2, '0');
            opt.textContent = opt.value;
            hourSel.appendChild(opt);
        }
        for (let m = 0; m < 60; m += 5) {
            const opt = document.createElement('option');
            opt.value = String(m).padStart(2, '0');
            opt.textContent = opt.value;
            minuteSel.appendChild(opt);
        }
        if (time) {
            const [h, m] = time.split(':');
            hourSel.value = h;
            minuteSel.value = m.padStart(2, '0');
        }
    }

    document.querySelectorAll('.start-hour').forEach(sel => {
        const minuteSel = sel.parentElement.querySelector('.start-minute');
        initTimeSelects(sel, minuteSel, sel.dataset.time);
    });

    document.querySelectorAll('.end-hour').forEach(sel => {
        const minuteSel = sel.parentElement.querySelector('.end-minute');
        initTimeSelects(sel, minuteSel, sel.dataset.time);
    });
});
