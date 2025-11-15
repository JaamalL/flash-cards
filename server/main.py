import requests
import time
from concurrent.futures import ThreadPoolExecutor, as_completed

url = "http://localhost:7777/auth/register"

def send_request(i):
    data = {
        "name": "Volodymyr",
        "email": f"zhurakwrewfgv-{i}@gmail.com",
        "password": "111"
    }
    start = time.perf_counter()
    try:
        r = requests.post(url, json=data, timeout=10)
        print(f"[{i}] Status: {r.status_code}, Time: {time.perf_counter()-start:.3f}s")
    except requests.RequestException as e:
        print(f"[{i}] Error: {e}, Time: {time.perf_counter()-start:.3f}s")
    time.sleep(0.2)

TOTAL_BATCHES = 50
BATCH_SIZE = 5

for batch_num in range(TOTAL_BATCHES):
    print(f"\n🚀 Надсилаємо батч {batch_num + 1}/{TOTAL_BATCHES}")
    start_index = batch_num * BATCH_SIZE
    end_index = start_index + BATCH_SIZE

    with ThreadPoolExecutor(max_workers=BATCH_SIZE) as executor:
        futures = [executor.submit(send_request, i) for i in range(start_index, end_index)]
        for f in as_completed(futures):
            pass  # усі результати вже друкуються в send_request

    time.sleep(0.2)
