// Calendar with month navigation

document.addEventListener('DOMContentLoaded', () => {//ページが全部読み込まれたら、中の処理を始める
    const container = document.getElementById('calendar');
    //innerHTML で HTMLの中身を上書きする
    container.innerHTML = `
        <div class="calendar-nav">
            <button id="prev-month">\u2190</button>
            <span id="calendar-title"></span>
            <button id="next-month">\u2192</button>
        </div>
        <div id="calendar-table"></div>
    `;

    const titleEl = container.querySelector('#calendar-title');
    const tableWrap = container.querySelector('#calendar-table');
    let current = new Date();

    //カレンダーを表示する処理,例えば renderCalendar(2025, 5) なら、「2025年6月」のカレンダーを作ります
    function renderCalendar(year, month) {
        const today = new Date();
        const firstDay = (new Date(year, month, 1).getDay() + 6) % 7;
        const lastDate = new Date(year, month + 1, 0).getDate();

        let html = '<table>';
        html += '<tr><th>月</th><th>火</th><th>水</th><th>木</th><th>金</th><th>土</th><th>日</th></tr><tr>';

        for (let i = 0; i < firstDay; i++) {
            html += '<td></td>';
        }

        for (let day = 1; day <= lastDate; day++) {
            if ((firstDay + day - 1) % 7 === 0 && day !== 1) {
                html += '</tr><tr>';
            }
            const isToday =
                day === today.getDate() &&
                month === today.getMonth() &&
                year === today.getFullYear();
            const cls = isToday ? ' class="today"' : '';
            html += '<td' + cls + '>' + day + '</td>';
        }

        const remaining = (firstDay + lastDate) % 7;
        if (remaining !== 0) {
            for (let i = 0; i < 7 - remaining; i++) {
                html += '<td></td>';
            }
        }
        html += '</tr></table>';
        tableWrap.innerHTML = html;
        titleEl.textContent = `${year}年${month + 1}月`;
        document.dispatchEvent(new Event('calendarRendered'));
    }

    //月を変更する関数
    function changeMonth(offset) {
        current.setMonth(current.getMonth() + offset);
        renderCalendar(current.getFullYear(), current.getMonth());
    }

    //ボタンクリック処理
    container.addEventListener('click', (e) => {
        if (e.target.id === 'prev-month') {
            changeMonth(-1);
        } else if (e.target.id === 'next-month') {
            changeMonth(1);
        }
    });

    //初期化
    renderCalendar(current.getFullYear(), current.getMonth());
});

