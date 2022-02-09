const btn = document.getElementsByClassName("btn btn-success");
let cartQuantity = document.getElementById("cart-quantity");
let currentCartQuantity;

// function getCartQuantity() {
//     fetch("http://localhost:8080/add-item", {
//         headers: { "Content-Type": "application/json" },
//     }).then(response => response.json())
//         .then(data => {
//             currentCartQuantity = data;
//             cartQuantity.textContent = currentCartQuantity;
//             addToCart();
//         })
// }
//
// getCartQuantity()

addToCart();


function addToCart() {
    for (let bt of btn) {
        bt.addEventListener('click', (event) => {
            console.log(event.target.value)
            const data = {productName: event.target.value}
            fetch("http://localhost:8080/add-item", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            })
                .then(res => {
                    console.log("Request complete! response:", res);
                });
            currentCartQuantity++;
            cartQuantity.textContent = currentCartQuantity;

        })
    }
}

