<?

use Flarum\Database\Migration;

return Migration::addColumns('users', [
    'points_count' => ['integer'],
    'points_probability' => ['text', 'nullable'],
]);
