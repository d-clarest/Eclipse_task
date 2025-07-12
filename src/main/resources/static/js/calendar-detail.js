document.addEventListener('DOMContentLoaded', () => {
  const overlay = document.getElementById('schedule-overlay');
  const popup = document.getElementById('schedule-popup');
  const closeBtn = document.getElementById('close-schedule-popup');
  const listEl = document.getElementById('schedule-popup-list');
  const titleEl = document.getElementById('schedule-popup-title');
  attach();//カレンダーの日付のあるセルをクリックしてイベント発動
  closeBtn.addEventListener('click', hide);//右上の×ボタンを押すと，ポップアップを非表示に
  overlay.addEventListener('click', hide);//ポップアップ以外の場所をクリックすると，ポップアップを非表示に
  document.addEventListener('calendarRendered', () => {
    // JavaScript側から dispatchEvent() で送信される カスタムイベント．機能的には不要だが、「意図を明示するコメント」
  });

  //カレンダーの日付のあるセルをクリックされたら
  function attach() {
    const table = document.getElementById('calendar');
    if (!table) return;//カレンダーがある画面でしか表示しないので
    table.addEventListener('click', (e) => {
      const td = e.target.closest('td');
      if (!td || !td.parentElement) return;//td が取得できなかった（クリックが空白など）場合や、親要素（行）がなければ中断。
      const day = parseInt(td.textContent, 10);
      if (isNaN(day)) return;
      const { year, month } = getYearMonth();//カレンダー上部のタイトルから年月を取得
      const dateStr = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;//年月日を "YYYY-MM-DD" 形式の文字列に整形
      console.log("DD");
      show(dateStr);//
    });
  }

  //カレンダー上部のタイトルから年月を取得する関数
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

  //バック背景をもとに戻し，ポップアップも非表示に
  function hide() {
    overlay.style.display = 'none';
    popup.style.display = 'none';
  }

  //dateStrの
  function show(dateStr) {
    listEl.innerHTML = '';
    const rows = document.querySelectorAll('.schedule-row');
    let items = [];
    const seen = new Set();
    rows.forEach((row) => {
      const schedDate = row.querySelector('.schedule-date-input').value;
      if (schedDate !== dateStr) return;//カレンダーの年月と一致しない予定はここで省く
      const addFlag = row.querySelector('.schedule-add-flag');
      const completed = !!row.querySelector('.completed-day-input')?.value;
      if (!completed && (!addFlag || !addFlag.checked)) return;//未予定かつaddのチェックがないのも省く
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
              console.log(items);

      if (id) seen.add(id);//配列と違って、同じ値を何度追加しても、1つしか保存されない
    });
    document.querySelectorAll('#calendar .schedules div')
      .forEach((div) => {
        if (div.dataset.date !== dateStr) return;//表示されている予定の data-date がクリックされた日付と一致しなければスキップ
        const id = div.dataset.id;
        if (id && seen.has(id)) return;//idがすでに処理済みならスキップ．同じ id の予定を 2回表示しないために
        const start = (div.dataset.start || '').slice(0, 5);//schedule.jsのdata-start から HH:mm の部分だけを取り出す
        const end = (div.dataset.end || '').slice(0, 5);
        const title = div.dataset.name || div.textContent;//nameは全部，textContentの方は6文字まで
        const completed =div.dataset.completed === 'true' || div.style.color === 'red';//完了済みの予定フラグ
        items.push({ start, end, title, completed });// カレンダーに表示されていた予定も items に追加。完了済みの予定を表示用だと思う
        console.log(items);
      });
    items.sort((a, b) => a.start.localeCompare(b.start));// 開始時刻順に並べ替え
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
    overlay.style.display = 'block';//バック背景表示
    popup.style.display = 'block';//ポップアップ表示
  }

});
