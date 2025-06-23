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

    const calendar = document.getElementById('calendar');
    let dayCells = {};
    let currentYear = new Date().getFullYear();
    let currentMonth = new Date().getMonth();

    function updateCurrentYearMonth() {
        const title = document.getElementById('calendar-title');
        if (title) {
            const m = title.textContent.match(/(\d+)年(\d+)月/);
            if (m) {
                currentYear = parseInt(m[1], 10);
                currentMonth = parseInt(m[2], 10) - 1;
            }
        }
    }

    function mapDayCells() {
        dayCells = {};
        updateCurrentYearMonth();
        calendar.querySelectorAll('td').forEach(td => {
            const day = parseInt(td.textContent, 10);
            if (!isNaN(day)) {
                dayCells[day] = td;
            }
        });
    }

    function addSchedule(name, dateStr) {
        const date = new Date(dateStr);
        if (isNaN(date.getTime())) return;
        if (date.getFullYear() !== currentYear || date.getMonth() !== currentMonth) return;
        const cell = dayCells[date.getDate()];
        if (!cell) return;
        let wrapper = cell.querySelector('.tasks');
        if (!wrapper) {
            wrapper = document.createElement('div');
            wrapper.className = 'tasks';
            cell.appendChild(wrapper);
        }
        const item = document.createElement('div');
        item.textContent = name.slice(0, 4);
        item.title = name;
        item.dataset.name = name;
        item.dataset.date = dateStr;
        wrapper.appendChild(item);
    }

    function removeSchedule(name, dateStr) {
        const date = new Date(dateStr);
        if (isNaN(date.getTime())) return;
        if (date.getFullYear() !== currentYear || date.getMonth() !== currentMonth) return;
        const cell = dayCells[date.getDate()];
        if (!cell) return;
        const wrapper = cell.querySelector('.tasks');
        if (!wrapper) return;
        wrapper.querySelectorAll('div').forEach(div => {
            if (div.dataset.name === name && div.dataset.date === dateStr) {
                div.remove();
            }
        });
    }

    function initSchedules() {
        mapDayCells();
        document.querySelectorAll('.schedule-row').forEach(row => {
            const cb = row.querySelector('.schedule-add-flag');
            if (cb && cb.checked) {
                const name = row.querySelector('.schedule-title-input').value;
                const date = row.querySelector('.schedule-date-input').value;
                addSchedule(name, date);
            }
        });
    }

    document.querySelectorAll('.schedule-date-input').forEach(inp => {
        const handler = () => {
            const date = new Date(inp.value);
            if (isNaN(date.getTime())) return;
            const dow = date.toLocaleDateString('ja-JP', { weekday: 'short' });
            const row = inp.closest('tr');
            if (row) {
                const dayField = row.querySelector('.schedule-day-of-week');
                if (dayField) {
                    dayField.value = dow;
                }
                const cb = row.querySelector('.schedule-add-flag');
                if (cb && cb.checked) {
                    removeSchedule(row.dataset.oldTitle, row.dataset.oldDate);
                    addSchedule(row.querySelector('.schedule-title-input').value, inp.value);
                }
                sendUpdate(row);
            }
        };
        inp.addEventListener('change', handler);
        inp.addEventListener('input', handler);
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
            if (!row) return;
            const title = row.querySelector('.schedule-title-input').value;
            const date = row.querySelector('.schedule-date-input').value;
            if (cb.checked) {
                addSchedule(title, date);
            } else {
                removeSchedule(title, date);
            }
            sendUpdate(row);
        });
    });

    document.querySelectorAll('.schedule-title-input').forEach(inp => {
        const handler = () => {
            const row = inp.closest('tr');
            if (row) {
                const cb = row.querySelector('.schedule-add-flag');
                if (cb && cb.checked) {
                    removeSchedule(row.dataset.oldTitle, row.dataset.oldDate);
                    addSchedule(inp.value, row.querySelector('.schedule-date-input').value);
                }
                sendUpdate(row);
            }
        };
        inp.addEventListener('change', handler);
        inp.addEventListener('input', handler);
    });

    document.querySelectorAll('.schedule-location-input').forEach(inp => {
        const handler = () => {
            const row = inp.closest('tr');
            if (row) sendUpdate(row);
        };
        inp.addEventListener('change', handler);
        inp.addEventListener('input', handler);
    });

    document.querySelectorAll('.schedule-detail-input').forEach(inp => {
        const handler = () => {
            const row = inp.closest('tr');
            if (row) sendUpdate(row);
        };
        inp.addEventListener('change', handler);
        inp.addEventListener('input', handler);
    });

    document.querySelectorAll('.schedule-feedback-input').forEach(inp => {
        const handler = () => {
            const row = inp.closest('tr');
            if (row) sendUpdate(row);
        };
        inp.addEventListener('change', handler);
        inp.addEventListener('input', handler);
    });

    document.querySelectorAll('.point-input').forEach(inp => {
        const handler = () => {
            const row = inp.closest('tr');
            if (row) sendUpdate(row);
        };
        inp.addEventListener('change', handler);
        inp.addEventListener('input', handler);
    });

    document.querySelectorAll('.completed-day-input').forEach(inp => {
        const handler = () => {
            const row = inp.closest('tr');
            if (row) sendUpdate(row);
        };
        inp.addEventListener('change', handler);
        inp.addEventListener('input', handler);
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

    const newButton = document.getElementById('new-schedule-button');
    if (newButton) {
        newButton.addEventListener('click', () => {
            const today = new Date();
            const dateStr = today.toISOString().split('T')[0];
            const dow = today.toLocaleDateString('ja-JP', { weekday: 'short' });
            const data = {
                addFlag: false,
                title: '',
                dayOfWeek: dow,
                scheduleDate: dateStr,
                startTime: '00:00',
                endTime: '01:00',
                location: '',
                detail: '',
                feedback: '',
                point: 0,
                completedDay: null
            };
            fetch('/schedule-add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            }).then(() => {
                location.reload();
            });
        });
    }

    initSchedules();
    document.addEventListener('calendarRendered', initSchedules);
});
