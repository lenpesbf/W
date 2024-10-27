document.addEventListener('DOMContentLoaded', function () {
    // Chọn tất cả các biểu tượng plus
    const plusIcons = document.querySelectorAll('.collection-item .plus');

    plusIcons.forEach(function (plus) {
        plus.addEventListener('click', function (event) {
            event.preventDefault(); // Ngăn hành động mặc định nếu có

            // Tìm phần tử cha .collection-item
            const collectionItem = plus.closest('.collection-item');

            // Tìm .size-container trong .collection-item này
            const sizeContainer = collectionItem.querySelector('.size-container');

            // Đóng tất cả các size-container khác
            document.querySelectorAll('.size-container.active').forEach(function (container) {
                if (container !== sizeContainer) {
                    container.classList.remove('active');
                }
            });

            // Toggle lớp 'active' để hiển thị/ẩn
            sizeContainer.classList.toggle('active');
        });
    });

    // Đóng size-container khi nhấp ra ngoài
    document.addEventListener('click', function (event) {
        plusIcons.forEach(function (plus) {
            const collectionItem = plus.closest('.collection-item');
            const sizeContainer = collectionItem.querySelector('.size-container');

            // Nếu click không phải là trong .collection-item, đóng size-container
            if (!collectionItem.contains(event.target)) {
                sizeContainer.classList.remove('active');
            }
        });
    });

    // Thêm sự kiện cho nút đóng
    const closeButtons = document.querySelectorAll('.size-container .close-btn');

    closeButtons.forEach(function (btn) {
        btn.addEventListener('click', function (event) {
            event.stopPropagation(); // Ngăn sự kiện lan truyền
            btn.closest('.size-container').classList.remove('active');
        });
    });

    // Xử lý chọn size
    const sizeButtons = document.querySelectorAll('.size-btn');

    sizeButtons.forEach(function (btn) {
        btn.addEventListener('click', function () {
            // Xóa lớp 'selected' khỏi tất cả các nút trong cùng size-container
            const sizeContainer = btn.closest('.size-container');
            const buttons = sizeContainer.querySelectorAll('.size-btn');
            buttons.forEach(function (button) {
                button.classList.remove('selected');
            });

            // Thêm lớp 'selected' vào nút được nhấp
            btn.classList.add('selected');
        });
    });
});


function selectImage(img) {
    // Xóa class 'selected' khỏi tất cả các hình ảnh
    const images = document.querySelectorAll('.image-cate img');
    images.forEach(image => {
        image.classList.remove('selected');
    });

    // Thêm class 'selected' cho hình ảnh đã được chọn
    img.classList.add('selected');
}