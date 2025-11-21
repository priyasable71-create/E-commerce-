document.addEventListener("DOMContentLoaded", () => {
    const addToCartButtons = document.querySelectorAll(".add-to-cart");

    addToCartButtons.forEach(button => {
        button.addEventListener("click", e => {
            e.preventDefault();

            const loggedIn = button.dataset.loggedIn === "true";

            if (!loggedIn) {
                alert("Please login to add items to cart!");
                window.location.href = "/login";
                return;
            }

            const productId = button.dataset.productId;

            fetch("/order/add?productId=" + productId, { method: "POST" })
                .then(res => {
                    if (res.redirected) {
                        window.location.href = res.url;
                    }
                })
                .catch(err => console.error(err));
        });
    });
});

