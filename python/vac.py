import copy, time, datetime, requests, sys, os, random
import winsound

def viable_options(resp, minimum_slots, min_age_booking, dose):
    options = []
    if len(resp['centers']) >= 0:
        for center in resp['centers']:
            for session in center['sessions']:
                # availability = session['available_capacity']
                availability = session['available_capacity_dose1'] if dose == 1 else session['available_capacity_dose2']
                if (availability >= minimum_slots) \
                        and (session['min_age_limit'] <= min_age_booking):
                    out = {
                        'name': center['name'],
                        'district': center['district_name'],
                        'pincode': center['pincode'],
                        'center_id': center['center_id'],
                        'available': availability,
                        'date': session['date'],
                        'slots': session['slots'],
                        'session_id': session['session_id']
                    }
                    if True and out['session_id'] != '18bd1762-277e-4cd8-b262-22101f56853c':
                        print("akdla")
                        options.append(out)

                else:
                    pass
    else:
        pass

    return options



request_header = {
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36',
    'origin': 'https://selfregistration.cowin.gov.in/',
    'referer': 'https://selfregistration.cowin.gov.in/',
    'authorization':''
}

url='https://cdn-api.co-vin.in/api/v2/appointment/sessions/calendarByDistrict?district_id=294&date=04-06-2021&vaccine=COVAXIN'
# url2='https://cdn-api.co-vin.in/api/v2/appointment/sessions/calendarByDistrict?district_id=294&date=03-06-2021&vaccine=COVAXIN'

def checkSlot():
    resp = requests.get(url, headers=request_header)
    options = []
    try:
        resp=resp.json()
        print(resp)
        options += viable_options(resp, 1, 18, 2)
    except Exception as e:
        print("Exception aaya",str(e))
        print(resp.content)
    return options

def alarm():
    for _ in range(1000):
        winsound.Beep(1000, 300)
        winsound.Beep(2000, 150)
        time.sleep(0.5)

while True:
    res = checkSlot()
    print(datetime.datetime.now(),"====================================================")
    print(res)
    if len(res) > 0:
        winsound.Beep(1000, 300)
        alarm()
    time.sleep(30)

