 // Hàm để toggle (hiện/ẩn) mục con của subcategory 1
 function toggleSubcategory(subcategoryId) {
    const subcategory = document.getElementById(`subcategory${subcategoryId}-extra`);
    if (subcategory.style.display === "none") {
        subcategory.style.display = "block"; // Hiển thị mục con
    } else {
        subcategory.style.display = "none"; // Ẩn mục con
    }
}


document.addEventListener("DOMContentLoaded", function () {
    const userIcon = document.querySelector('.user-icon');
    const userOptions = document.querySelector('.user-options');

    userIcon.addEventListener('click', function () {
        // Toggle hiển thị menu
        if (userOptions.style.display === 'flex') {
            userOptions.style.display = 'none';
            userOptions.style.opacity = '0';
        } else {
            userOptions.style.display = 'flex';
            userOptions.style.opacity = '1';
        }
    });

    // Đóng menu nếu click ra bên ngoài
    document.addEventListener('click', function (event) {
        if (!userMenu.contains(event.target)) {
            userOptions.style.display = 'none';
            userOptions.style.opacity = '0';
        }
    });
});



window.addEventListener('scroll', () => {
    const navbar = document.querySelector('.shop-navbar');
    if (window.scrollY > 50) {
        navbar.classList.add('scrolled');
    } else {
        navbar.classList.remove('scrolled');
    }
});