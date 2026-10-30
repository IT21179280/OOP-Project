/* =====================================================================
   Online Event Planning System - small front-end enhancements
   ===================================================================== */
(function () {
    "use strict";

    function onReady(callback) {
        if (document.readyState === "loading") {
            document.addEventListener("DOMContentLoaded", callback);
        } else {
            callback();
        }
    }

    /* -----------------------------------------------------------------
       Mobile navigation
       ----------------------------------------------------------------- */
    function setupNavigation() {
        var toggle = document.querySelector(".nav-toggle");
        var nav = document.querySelector(".main-nav");
        if (!toggle || !nav) {
            return;
        }

        toggle.addEventListener("click", function () {
            var open = nav.classList.toggle("open");
            toggle.setAttribute("aria-expanded", open ? "true" : "false");
        });

        // Close the menu when a link is selected (handy on small screens).
        nav.addEventListener("click", function (event) {
            if (event.target.tagName === "A" && nav.classList.contains("open")) {
                nav.classList.remove("open");
                toggle.setAttribute("aria-expanded", "false");
            }
        });
    }

    /* -----------------------------------------------------------------
       Confirmation messages for delete buttons
       ----------------------------------------------------------------- */
    function setupConfirmations() {
        var forms = document.querySelectorAll("form[data-confirm]");
        Array.prototype.forEach.call(forms, function (form) {
            form.addEventListener("submit", function (event) {
                var message = form.getAttribute("data-confirm");
                if (message && !window.confirm(message)) {
                    event.preventDefault();
                }
            });
        });
    }

    /* -----------------------------------------------------------------
       Image preview for file inputs
       ----------------------------------------------------------------- */
    function setupImagePreviews() {
        var inputs = document.querySelectorAll('input[type="file"][accept*="image"]');
        Array.prototype.forEach.call(inputs, function (input) {
            input.addEventListener("change", function () {
                var file = input.files && input.files[0];
                var preview = input.parentNode.querySelector(".image-preview");

                if (!file || !/^image\//.test(file.type)) {
                    if (preview) {
                        preview.remove();
                    }
                    return;
                }

                if (!preview) {
                    preview = document.createElement("div");
                    preview.className = "image-preview";
                    var image = document.createElement("img");
                    image.alt = "Selected image preview";
                    preview.appendChild(image);
                    input.parentNode.insertBefore(preview, input.nextSibling);
                }

                var reader = new FileReader();
                reader.onload = function (event) {
                    preview.querySelector("img").src = event.target.result;
                };
                reader.readAsDataURL(file);
            });
        });
    }

    /* -----------------------------------------------------------------
       Password confirmation on the registration form
       ----------------------------------------------------------------- */
    function setupPasswordCheck() {
        var form = document.getElementById("registerForm");
        if (!form) {
            return;
        }

        var password = form.querySelector("#password");
        var confirmPassword = form.querySelector("#confirmPassword");
        if (!password || !confirmPassword) {
            return;
        }

        function check() {
            if (confirmPassword.value && password.value !== confirmPassword.value) {
                confirmPassword.setCustomValidity("The two passwords do not match.");
            } else {
                confirmPassword.setCustomValidity("");
            }
        }

        password.addEventListener("input", check);
        confirmPassword.addEventListener("input", check);
        form.addEventListener("submit", check);
    }

    /* -----------------------------------------------------------------
       Event form: the end time must be after the start time
       ----------------------------------------------------------------- */
    function setupEventTimeCheck() {
        var form = document.getElementById("eventForm");
        if (!form) {
            return;
        }

        var start = form.querySelector("#startTime");
        var end = form.querySelector("#endTime");
        if (!start || !end) {
            return;
        }

        function check() {
            if (start.value && end.value && end.value <= start.value) {
                end.setCustomValidity("The end time must be after the start time.");
            } else {
                end.setCustomValidity("");
            }
        }

        start.addEventListener("change", check);
        end.addEventListener("change", check);
        form.addEventListener("submit", check);
    }

    /* -----------------------------------------------------------------
       Advertisement form: fill event details from the related event
       ----------------------------------------------------------------- */
    function setupRelatedEventFill() {
        var select = document.getElementById("relatedEvent");
        if (!select) {
            return;
        }

        select.addEventListener("change", function () {
            var option = select.options[select.selectedIndex];
            if (!option || !option.value) {
                return;
            }

            var nameField = document.getElementById("eventName");
            var dateField = document.getElementById("eventDate");
            var locationField = document.getElementById("location");

            if (nameField && option.getAttribute("data-name")) {
                nameField.value = option.getAttribute("data-name");
            }
            if (dateField && option.getAttribute("data-date")) {
                dateField.value = option.getAttribute("data-date");
            }
            if (locationField && option.getAttribute("data-location")) {
                locationField.value = option.getAttribute("data-location");
            }
        });
    }

    /* -----------------------------------------------------------------
       Fade out success and information messages after a few seconds
       ----------------------------------------------------------------- */
    function setupAlertFade() {
        var alerts = document.querySelectorAll(".alert-success, .alert-info");
        Array.prototype.forEach.call(alerts, function (alert) {
            window.setTimeout(function () {
                alert.style.transition = "opacity 0.5s ease";
                alert.style.opacity = "0";
                window.setTimeout(function () {
                    if (alert.parentNode) {
                        alert.parentNode.removeChild(alert);
                    }
                }, 500);
            }, 7000);
        });
    }

    onReady(function () {
        setupNavigation();
        setupConfirmations();
        setupImagePreviews();
        setupPasswordCheck();
        setupEventTimeCheck();
        setupRelatedEventFill();
        setupAlertFade();
    });
})();
