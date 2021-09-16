// let slideIndex = 1;
window.slideIndex = 1;

showSlides(window.slideIndex);

// Next/previous controls
window.plusSlides = {
    plusSlides_: function (n) {
        showSlides(window.slideIndex += n);
    }
}

// Thumbnail image controls
window.currentSlide = {

    currentSlide: function (n) {
        showSlides(window.slideIndex = n);
    }
}

window.showSlides = {
    showSlides_: function (n) {
        let i;
        const slides = document.getElementsByClassName("mySlides");
        const dots = document.getElementsByClassName("dot");
        if (n > slides.length) {
            window.slideIndex = 1
        }
        if (n < 1) {
            window.slideIndex = slides.length
        }
        for (i = 0; i < slides.length; i++) {
            slides[i].style.display = "none";
        }
        for (i = 0; i < dots.length; i++) {
            dots[i].className = dots[i].className.replace(" active", "");
        }
        slides[window.slideIndex - 1].style.display = "block";
        dots[window.slideIndex - 1].className += " active";
    }
}