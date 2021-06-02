# Points

![License](https://img.shields.io/badge/license-MIT-blue.svg) [![Latest Stable Version](https://img.shields.io/packagist/v/hehongyuanlove/flarum-points.svg)](https://packagist.org/packages/hehongyuanlove/flarum-points)

A [Flarum](http://flarum.org) extension.  points copy

### Installation

Use [Bazaar](https://discuss.flarum.org/d/5151-flagrow-bazaar-the-extension-marketplace) or install manually with composer:

```sh
composer require hehongyuanlove/xxxxxx
```

### Updating

```sh
composer update hehongyuanlove/xxxxxxx
```

### Links

- [Packagist](https://packagist.org/packages/hehongyuanlove/flarum-points)

### 安装说明

- 删除 `migrations` 表中的 `2020_06_17_102253_add_points_to_users` `2020_06_17_081739_points_logs_table` 记录

  ```sh
    DELETE FROM `xxx_migrations` WHER `migration` = "2020_06_17_102253_add_points_to_users"
    DELETE FROM `xxx_migrations` WHER `migration` = "2020_06_17_081739_points_logs_table"
  ```
- 删除原先的 `gam_points_logs` 表
- 删除 `user` 表当中得 `points_probability` `points_count` 字段
