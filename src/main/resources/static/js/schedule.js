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

    const list = document.getElementById('time-5min');
    if (list) {
        for (let h = 0; h < 24; h++) {
            for (let m = 0; m < 60; m += 5) {
                const option = document.createElement('option');
                option.value = `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}`;
                list.appendChild(option);
            }
        }
    }

    document.querySelectorAll('input[type="time"]').forEach(inp => {
        inp.addEventListener('change', () => {
            if (!inp.value) return;
            let [h, m] = inp.value.split(':').map(Number);
            m = Math.round(m / 5) * 5;
            if (m === 60) {
                m = 0;
                h = (h + 1) % 24;
            }
            inp.value = `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}`;
        });
    });
});
