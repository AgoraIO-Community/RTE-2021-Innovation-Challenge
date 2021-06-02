#!/usr/bin/python
# -*- coding: UTF-8 -*-
import re
import os
import shutil

replaceKeyValues = {
    "app_id": "<#Your_Value#>",
    "leancloud_app_id": "<#Your_Value#>",
    "leancloud_app_key": "<#Your_Value#>",
    "leancloud_server_url": "<#Your_Value#>",
}


def main():
    with open("./app/src/main/res/values/strings_config.xml",
              'r+',
              encoding='utf-8') as file:

        lines = []
        for line in file.readlines():
            for key, value in replaceKeyValues.items():
                if key in line:
                    pattern = re.compile(r'(?<=>).+(?=<)')
                    line = re.sub(pattern, value, line)
                    break

            lines.append(line)

        file.seek(0)
        file.truncate()
        file.writelines(lines)
        file.flush()


if __name__ == "__main__":
    main()
    print("Done..")
