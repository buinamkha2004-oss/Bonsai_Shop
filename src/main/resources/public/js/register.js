// Toggle hiện/ẩn mật khẩu
const togglePassword = document.getElementById('togglePassword');
const passwordInput = document.getElementById('password');

togglePassword.addEventListener('click', function () {
    const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
    passwordInput.setAttribute('type', type);
    this.textContent = type === 'password' ? '👁️' : '🙈';
});

// Đo độ mạnh mật khẩu
passwordInput.addEventListener('input', function () {
    const val = this.value;
    const bar = document.getElementById('strengthBar');
    const text = document.getElementById('strengthText');
    let strength = 0;

    if (val.length >= 6) strength++;
    if (val.length >= 10) strength++;
    if (/[A-Z]/.test(val)) strength++;
    if (/[0-9]/.test(val)) strength++;
    if (/[^A-Za-z0-9]/.test(val)) strength++;

    const levels = [
        { width: '0%',   color: '#e0e0e0', text: '' },
        { width: '25%',  color: '#e74c3c', text: 'Yếu' },
        { width: '50%',  color: '#f39c12', text: 'Trung bình' },
        { width: '75%',  color: '#3498db', text: 'Khá mạnh' },
        { width: '90%',  color: '#2ecc71', text: 'Mạnh' },
        { width: '100%', color: '#27ae60', text: 'Rất mạnh' },
    ];

    bar.style.width = levels[strength].width;
    bar.style.background = levels[strength].color;
    text.textContent = levels[strength].text;
    text.style.color = levels[strength].color;
});

// Validate realtime
const fullNameInput = document.getElementById('fullName');
const emailInput = document.getElementById('email');
const phoneInput = document.getElementById('phone');

fullNameInput.addEventListener('blur', function () {
    const err = document.getElementById('fullNameError');
    if (this.value.trim().length < 6) {
        this.classList.add('error');
        err.style.display = 'block';
        err.textContent = 'Họ tên phải có ít nhất 6 ký tự!';
    } else {
        this.classList.remove('error');
        this.classList.add('success');
        err.style.display = 'none';
    }
});

emailInput.addEventListener('blur', function () {
    const err = document.getElementById('emailError');
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.value)) {
        this.classList.add('error');
        err.style.display = 'block';
        err.textContent = 'Email không hợp lệ!';
    } else {
        this.classList.remove('error');
        this.classList.add('success');
        err.style.display = 'none';
    }
});

phoneInput.addEventListener('blur', function () {
    const err = document.getElementById('phoneError');
    const phoneRegex = /^[0-9]{10,11}$/;
    if (this.value && !phoneRegex.test(this.value)) {
        this.classList.add('error');
        err.style.display = 'block';
        err.textContent = 'Số điện thoại không hợp lệ!';
    } else {
        this.classList.remove('error');
        if (this.value) this.classList.add('success');
        err.style.display = 'none';
    }
});

// Hiệu ứng nút register
// Hiệu ứng nút register - để form submit trước
const btnRegister = document.querySelector('.btn-register');
btnRegister.addEventListener('click', function (e) {
    const hasError = document.querySelectorAll('.error-msg[style="display: block;"]').length > 0;
    if (hasError) {
        e.preventDefault(); // chặn submit nếu có lỗi validation
        return;
    }
    // Để form submit bình thường, chỉ đổi text
    this.textContent = 'Đang đăng ký...';
});