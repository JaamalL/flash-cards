# import requests
# from concurrent.futures import ThreadPoolExecutor, as_completed
# import time
#
# url = "http://localhost:7777/auth/login"
# TOTAL = 200
# BATCH_SIZE = 20
# BATCH_DELAY = 1  # ⏱️ затримка між батчами в секундах
#
# def send_request(i):
#     data = {
#         "email": f"zhurakw-{i}@gmail.com",
#         "password": "111"
#     }
#     try:
#         response = requests.post(url, json=data, timeout=5)
#         return i, response.status_code, response.text
#     except requests.exceptions.RequestException as e:
#         return i, None, str(e)
#
# for start in range(0, TOTAL, BATCH_SIZE):
#     end = min(start + BATCH_SIZE, TOTAL)
#     print(f"\n🚀 Надсилаємо батч {start+1}-{end}")
#
#     with ThreadPoolExecutor(max_workers=BATCH_SIZE) as executor:
#         futures = [executor.submit(send_request, i) for i in range(start, end)]
#
#         for future in as_completed(futures):
#             i, status, result = future.result()
#             print(f"[{i+1}] Status: {status}, Result: {result}")
#
#     if end < TOTAL:
#         print(f"⏳ Очікуємо {BATCH_DELAY} сек перед наступним батчем...\n")
#         time.sleep(BATCH_DELAY)


import requests
from concurrent.futures import ThreadPoolExecutor, as_completed
import time

url = "http://localhost:7777/auth/register"
TOTAL = 200
BATCH_SIZE = 20
BATCH_DELAY = 1  # затримка між батчами в секундах

def send_request(i):
    data = {
        "name": "Volodymyr",
        "email": f"zhurakw-{i}@gmail.com",
        "password": "111"
    }
    start_time = time.perf_counter()
    try:
        response = requests.post(url, json=data, timeout=5)
        elapsed = time.perf_counter() - start_time
        return i, response.status_code, response.text, elapsed
    except requests.exceptions.RequestException as e:
        elapsed = time.perf_counter() - start_time
        return i, None, str(e), elapsed

for start in range(0, TOTAL, BATCH_SIZE):
    end = min(start + BATCH_SIZE, TOTAL)
    print(f"\n🚀 Надсилаємо батч {start+1}-{end}")

    with ThreadPoolExecutor(max_workers=BATCH_SIZE) as executor:
        futures = [executor.submit(send_request, i) for i in range(start, end)]

        for future in as_completed(futures):
            i, status, result, elapsed = future.result()
            print(f"[{i+1}] Status: {status}, Time: {elapsed:.3f}s, Result: {result}")

    if end < TOTAL:
        print(f"⏳ Очікуємо {BATCH_DELAY} сек перед наступним батчем...\n")
        time.sleep(BATCH_DELAY)
