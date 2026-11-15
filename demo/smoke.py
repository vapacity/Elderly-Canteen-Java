"""Exercise checkout with reserved demo accounts 10 and 11."""
import json
import os
import sys
from urllib.request import Request, urlopen

base = sys.argv[1].rstrip('/') + '/demo-api'
password = os.environ['CANTEEN_DEMO_PASSWORD']


def request(route, payload=None, token=None):
    headers = {'Content-Type': 'application/json'}
    if token:
        headers['Authorization'] = 'Bearer ' + token
    data = None if payload is None else json.dumps(payload).encode()
    with urlopen(Request(base + route, data, headers), timeout=20) as response:
        return json.load(response)


def login(number):
    return request('/account/login', {'phoneNum': f'199000000{number:02}', 'password': password})['response']['token']


def check_order(number, success):
    token = login(number)
    cart = request('/cart/createCart', {}, token)['response']['cartId']
    request('/cart/clearCart', {'cartId': cart}, token)
    assert request('/cart/updateCartItem', {'cartId': cart, 'dishId': 6, 'quantity': 2}, token)['success'] is False
    assert request('/cart/updateCartItem', {'cartId': cart, 'dishId': 3, 'quantity': 1}, token)['success']
    before = next(x for x in request('/order/getMenuToday')['menu'] if x['dishId'] == 3)['stock']
    balance = request('/account/getPersonInfo', token=token)['response']['money']
    address = '冒烟验证用虚构地址'
    result = request('/cart/ensureCart', {'cartId': cart, 'deliverOrDining': True,
                      'newAddress': address, 'setDefaultAdd': False}, token)
    assert result['success'] is success, result
    after = next(x for x in request('/order/getMenuToday')['menu'] if x['dishId'] == 3)['stock']
    new_balance = request('/account/getPersonInfo', token=token)['response']['money']
    if success:
        assert before - after == 1 and balance - new_balance == 2
        latest = request('/order/getPastOrder', token=token)['response'][0]
        info = request(f"/order/getOrderMsg?OrderId={latest['orderId']}", token=token)['response']
        assert info['cusAddress'] == address and info['deliverStatus'] == '待接单'
    else:
        assert result['msg'] == '余额不足' and before == after and balance == new_balance
        assert request(f'/cart/getCartItem{cart}', token=token)['menu'][0]['quantity'] == 1
        request('/cart/clearCart', {'cartId': cart}, token)


check_order(10, False)
check_order(11, True)
print('PASS: low stock, insufficient balance rollback, checkout, debit, inventory and order detail')
