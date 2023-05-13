from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait

import sqlite3
from sqlite3 import Error

def connection():
    try:
        con = sqlite3.connect('test.db')
        return con
    except Error:
        print(Error)


def check_tabel(con):
    cursor_db = con.cursor()
    cursor_db.execute('SELECT cardno from tlqkf where id = 5 ')
    return cursor_db.fetchall()


con = connection()
check_tabel(con)

driver = webdriver.Chrome()

wait = WebDriverWait(driver, 10)

driver.get("https://www.purmeecard.com/index2.jsp")

check_elem = wait.until(
    EC.element_to_be_clickable((By.XPATH, "/html/body/section[1]/div/div/div/form/div/div[4]/div/input")))
check_elem.click()

driver.switch_to.frame("laybox-frame")

id_elem = wait.until(EC.element_to_be_clickable((By.ID, "card")))
id_elem.send_keys(1036745201030364)
id_elem.send_keys(Keys.ENTER)

table = driver.find_element_by_class_name('text_title')
tbody = table.find_element_by_tag_name("tbody")
rows = tbody.find_elements_by_tag_name("tr")
for index, value in enumerate(rows):
    body = value.find_elements_by_tag_name("td")[0]
    print(body.text)
    print(body.text, file=open('result.txt', 'a', encoding='utf-8'))
