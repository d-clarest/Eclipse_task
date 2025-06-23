document.addEventListener('DOMContentLoaded', () => {
    function sendUpdate(row) {
        const data = {
            oldTitle: row.dataset.oldTitle,
            oldScheduleDate: row.dataset.oldDate,
            addFlag: row.querySelector('.schedule-add-flag').checked,
            title: row.querySelector('.schedule-title-input').value,
            dayOfWeek: row.querySelector('.schedule-day-of-week').value,
            scheduleDate: row.querySelector('.schedule-date-input').value,
            startTime: row.querySelector('.start-hour').value.padStart(2, '0') + ':' +
                       row.querySelector('.start-minute').value.padStart(2, '0'),
            endTime: row.querySelector('.end-hour').value.padStart(2, '0') + ':' +
                     row.querySelector('.end-minute').value.padStart(2, '0'),
            location: row.querySelector('.schedule-location-input').value,
            detail: row.querySelector('.schedule-detail-input').value,
            feedback: row.querySelector('.schedule-feedback-input').value,
            point: row.querySelector('.point-input').value,
            completedDay: row.querySelector('.completed-day-input').value || null
        };
        fetch('/schedule-update', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        }).then(() => {
            row.dataset.oldTitle = data.title;
            row.dataset.oldDate = data.scheduleDate;
        });
    }

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
                sendUpdate(row);
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
        sel.addEventListener('change', () => {
            const row = sel.closest('tr');
            if (row) sendUpdate(row);
        });
        if (minuteSel) {
            minuteSel.addEventListener('change', () => {
                const row = sel.closest('tr');
                if (row) sendUpdate(row);
            });
        }
    });

    document.querySelectorAll('.end-hour').forEach(sel => {
        const minuteSel = sel.parentElement.querySelector('.end-minute');
        initTimeSelects(sel, minuteSel, sel.dataset.time);
        sel.addEventListener('change', () => {
            const row = sel.closest('tr');
            if (row) sendUpdate(row);
        });
        if (minuteSel) {
            minuteSel.addEventListener('change', () => {
                const row = sel.closest('tr');
                if (row) sendUpdate(row);
            });
        }
    });

    document.querySelectorAll('.schedule-add-flag').forEach(cb => {
        cb.addEventListener('change', () => {
            const row = cb.closest('tr');
            if (row) sendUpdate(row);
        });
    });

    document.querySelectorAll('.schedule-title-input').forEach(inp => {
        inp.addEventListener('change', () => {
            const row = inp.closest('tr');
            if (row) sendUpdate(row);
        });
    });

    document.querySelectorAll('.schedule-location-input').forEach(inp => {
        inp.addEventListener('change', () => {
            const row = inp.closest('tr');
            if (row) sendUpdate(row);
        });
    });

    document.querySelectorAll('.schedule-detail-input').forEach(inp => {
        inp.addEventListener('change', () => {
            const row = inp.closest('tr');
            if (row) sendUpdate(row);
        });
    });

    document.querySelectorAll('.schedule-feedback-input').forEach(inp => {
        inp.addEventListener('change', () => {
            const row = inp.closest('tr');
            if (row) sendUpdate(row);
        });
    });

    document.querySelectorAll('.point-input').forEach(inp => {
        inp.addEventListener('change', () => {
            const row = inp.closest('tr');
            if (row) sendUpdate(row);
        });
    });

    document.querySelectorAll('.completed-day-input').forEach(inp => {
        inp.addEventListener('change', () => {
            const row = inp.closest('tr');
            if (row) sendUpdate(row);
        });
    });

    document.querySelectorAll('.complete-button').forEach(btn => {
        const row = btn.closest('tr');
        const comp = row ? row.querySelector('.completed-day-input') : null;
        if (comp && comp.value) {
            btn.value = '取消';
        }

        btn.addEventListener('click', () => {
            if (!row || !comp) return;

            if (btn.value === '完了') {
                const today = new Date().toISOString().split('T')[0];
                comp.value = today;
                btn.value = '取消';
            } else {
                comp.value = '';
                btn.value = '完了';
            }
            sendUpdate(row);
        });
    });
});
