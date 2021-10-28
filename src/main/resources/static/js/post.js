let isAdded = false;

function gridClickEvent() {
    // 클릭된 rowkey를 가져오고 해당 row의 id column 값을 가져와서 이동시키기
    grid.on('click', ev => {
        if (ev.rowKey > -1) {
            const id = grid.getFormattedValue(ev.rowKey, 'id'); // (rowKey, columnName)
            location.href = '/post/bbs/view?id=' + id;
        }
    });
}

// 게시글 1개를 눌렀을 때 동작하는 함수
$(document).on('click', 'td[data-column-name=bbsTitle]', function () {
    const td = $(this).siblings()[0];
    const bid = td.children[0].innerHTML;

    $.ajax({ // 조회수 증가 코드
        type: 'POST',
        url: '/api/bbs/update/views',
        dataType: 'json',
        data: JSON.stringify({"id": bid}),
        contentType: 'application/json; charset=utf-8',
        error: function () {
            console.log('view update failed.');
        }
    })

    location.href = '/post/bbs/view?id=' + bid;
});

let urlList = []; // 사진 src만 갖고 있는 배열

function deleteBbs(bbsId, categoryId) { // 게시물 삭제
    $('#deleteBbsBtn').on('click', function () {

        const result = confirm('게시글을 삭제하시겠습니까?');
        if (result) {
            const address = '/post/bbs?category=' + categoryId;
            let data = new Object();
            data.id = bbsId;

            const imgList = $('#bbsContents img');
            if (imgList != null) {
                urlList = [];
                for (let i = 0; i < imgList.length; i++) {
                    const startIndex = imgList[i].currentSrc.indexOf('/summernoteImg/');
                    urlList.push(imgList[i].currentSrc.substring(startIndex, imgList[i].currentSrc.length));
                }
                data.urls = urlList;
            }
            console.log(data);
            $.ajax({
                method: 'DELETE',
                data: JSON.stringify(data),
                contentType: 'application/json;charset=utf-8;',
                url: '/api/bbs/delete',
                success: function () {
                    location.href = address;
                }
            })
        } else {
            $(this).focused = false;
        }
    })
}

function sortBbs() { // 정렬
    $('#sortStandard').on('change', function () {
        sortType = $('#sortStandard option:selected').val();
        grid.readData(1, { "column" : sortType });
    });
}

function filterGrid() { // 검색 이벤트 핸들러
    const input = $('#searchInput');
    const selectedValue = $('#findStandard option:selected').val();

    if (input.val() === '') {
        alert('내용을 입력해주세요.');
    } else {
        grid.readData(1, {"searchType" : selectedValue, "keyword" : input.val() });
    }
}

$('#searchImg').on('click', function () { filterGrid(); });
$('#searchInput').on('keydown', function (key) {
    if (key.key === 'Enter') {
        filterGrid();
    }
});

function replyListener() {
    let isShowReply = false;
    $('#replyBtn').on('click', function () {
        if (isShowReply) {
            isShowReply = false;
            $('#replyContainer').hide();
        } else {
            isShowReply = true;
            $('#replyContainer').show();
        }
    });
}

function clickLikeBtn(bbsId) { // Like 버튼을 눌렀을 때 동작하는 함수
    $('#likeBtn').on('click', function () {
        const imgSrc = $('#likeBtn img').attr('id');
        let type = '';

        if (imgSrc === 'heartImg') { // 현재 이미지의 id가 heartImg = 좋아요 클릭된 상태
            type = 'cancel';
        } else { // 좋아요 취소한 상태
            type = 'like';
        }

        $.ajax({
            url: '/api/heart/like',
            method: 'POST',
            data: JSON.stringify({ "bbsId" : bbsId, "type" : type }),
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            success: function (jsonResult) {
                if (jsonResult.resultCode === 0) {
                    if (type === 'cancel') {
                        $('#likeBtn img').attr('src', '/img/emptyHeart.png');
                        $('#likeBtn img').attr('id', 'emptyHeartImg');
                    } else {
                        $('#likeBtn img').attr('src', '/img/heart.png');
                        $('#likeBtn img').attr('id', 'heartImg');
                    }
                    const likeText = $('span#likeCntText');
                    console.log(likeText);

                    $('span#likeCntText')[0].innerHTML = jsonResult.likeCnt;
                } else if (jsonResult.resultCode === -1) {
                    alert('like Count를 업데이트 하던 중 오류가 발생했습니다.');
                }
            }
        })
    });
}

function showMapModal(lat, lng) {
    $('#locationInfo').on('click', function () {
        initMap(lat, lng, 'saved');
        $('#mapDialog').modal('show');
    });
}

//////////////////////////////////
// map에 추가/삭제
function clickAddMapBtn(favObj, bbsId, lat, lng) {
    const addBtn = $('#addMap'); // map btn

    if (favObj) { // 추가했는지 여부
        isAdded = true;
    } else {
        isAdded = false;
    }

    const placeName = $('.placeName')[0].innerHTML;

    addBtn.on('click', function (e) {
        e.stopPropagation(); // 버블링 중단
        // bbsId, type(add/cancel), lat, lng
        const data = {
            "bbsId" : bbsId,
            "lat" : lat,
            "lng": lng,
            "placeName" : placeName
        }
        if (!isAdded) { // 추가 x -> o
            data.type = "add";
        } else {
            data.type = "cancel";
        }
        $.ajax({
            url: '/api/fav/map',
            method: 'POST',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=utf-8',
            dataType: "json",
            success: function (mapData) {
                if (mapData.status === "OK") {
                    // map에 추가되었습니다. 확인창 표시
                    // + 버튼을 - 버튼으로 바꾸기
                    while(addBtn[0].hasChildNodes()) {
                        addBtn[0].removeChild(addBtn[0].lastChild);
                    }

                    if (data.type === "add") {
                        alert('Map에 추가되었습니다. 상단의 Map 메뉴에서 확인해주세요.');
                        isAdded = true;
                        addBtn.prepend('<i class="fas fa-minus me-3"></i>' +
                            '<span class="tooltip-text">Map에서 제거하기</span>');
                    } else {
                        alert('Map에서 제거되었습니다. 상단의 Map 메뉴에서 확인해주세요.');
                        isAdded = false;
                        addBtn.prepend('<i class="fas fa-plus me-3"></i>' +
                            '<span class="tooltip-text">Map에 추가하기</span>');
                    }
                } else {
                    alert('오류가 발생하였습니다. 잠시후 다시 시도해주십시오.');
                }
            }
        });
    });
}
