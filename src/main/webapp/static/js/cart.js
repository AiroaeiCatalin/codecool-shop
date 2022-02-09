const totalPrice = document.getElementsByClassName("font-bold")[0]
const items = document.getElementsByClassName("ibox")[0];

function getCartQuantity() {
    fetch("http://localhost:8080/cart", {
        headers: { "Content-Type": "application/json" },
    }).then(response => response.json())
        .then(data => {
            let newTotalPrice = 0;
            for (let [k, v] of Object.entries(data)) {
                const imageSrc = `"/static/img/product_${v[3]}.jpg"`;
                const el = `<div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table shoping-cart-table">
                                <tbody>
                                <tr>
                                    <td width="90">
                                        <img class="" height="90" width="90" src=${imageSrc} alt="" />

                                    </td>
                                    <td class="desc">
                                        <h3>
                                            <a href="#" class="text-navy">
                                                ${k}
                                            </a>
                                        </h3>
                                        <dl class="small m-b-none" data-quantity="${k}">
                                            <button type="button" class="btn btn-danger">-</button><button type="button" class="btn btn-success">+</button>
                                        </dl>
                                    </td>
                                    <td>
                                        <span>Unit: </span>${v[2]}
                                    </td>
                                    <td width="65">
                                        <span>Quantity: </span><strong>${v[0]}</strong>
                                    </td>
                                    <td>
                                        <h4>
                                            ${v[2] * v[0]}
                                        </h4>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>`

                items.innerHTML += el;

                newTotalPrice += parseInt(v[2] * v[0])

            }
            const buttonsPanel = `        <div class="ibox-content">
                        <button class="btn btn-primary pull-right" onclick="location.href='/checkout'"><i class="fa fa fa-shopping-cart"></i> Checkout</button>
                        <button class="btn btn-white" onclick="location.href='/'"><i class="fa fa-arrow-left"></i> Continue shopping</button>
                    </div>`
            console.log(buttonsPanel)
            items.innerHTML +=buttonsPanel;
            totalPrice.textContent = newTotalPrice.toString();
            const minuses = document.getElementsByClassName("btn btn-danger");
            const pluses = document.getElementsByClassName("btn btn-success")
            modifyCartQuantity(minuses, "subtraction")
            modifyCartQuantity(pluses, "addition")
        })

}

function modifyCartQuantity(buttons, operation) {
    for (let btn of buttons) {
        btn.addEventListener('click', function () {
            const selectedItem = btn.parentElement.getAttribute("data-quantity");
            let data;
            if (operation === "addition") {
                data = {addition: selectedItem}
            } else {
                data = {subtraction: selectedItem}
            }
            fetch("http://localhost:8080/cart", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            })
                .then(res => {
                    console.log("Request complete! response:", res);
                });
            reloadItems();
        })
    }
}

function reloadItems() {
    items.innerHTML = "";
    getCartQuantity();
}

getCartQuantity()