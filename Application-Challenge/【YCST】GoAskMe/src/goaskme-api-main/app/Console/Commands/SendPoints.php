<?php

namespace App\Console\Commands;

use App\Models\Classes;
use App\Models\ClassPassword;
use App\Models\PointsRecord;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\Log;

class SendPoints extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
    protected $signature = 'points:send';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = '转存积分';
    const TAX_RATE = 0.01;
    const GAM = 1;
    const GAM_NAME = 'GAM';

    /**
     * Create a new command instance.
     *
     * @return void
     */
    public function __construct()
    {
        parent::__construct();
    }

    /**
     * Execute the console command.
     *
     * @return int
     */
    public function handle()
    {
        $todayEnd = date("Y-m-d")." 23:59:59";
        $todayStart = date("Y-m-d")." 00:00:00";
        $classes = Classes::query()
            ->where("end_time",">=",$todayStart)
            ->where("end_time","<",$todayEnd)
            ->whereNotNull("user_id")
            ->get();
        $bar = $this->output->createProgressBar(count($classes));

        $bar->start();
        foreach ($classes as $class) {
            try {
                $bar->advance();

                $num = ClassPassword::query()->where("class_id","=",$class->id)->count();
                $salesMoney = $class->price * $num;
                if ($salesMoney <= 0)
                    continue;
                if ($salesMoney > 100){
                    $tax = floor($salesMoney * self::TAX_RATE);
                }elseif ($salesMoney < 1){
                    $tax = 0;
                }else{
                    $tax = 1;
                }
                $income = $salesMoney - $tax;
                $current = PointsRecord::checkPoint($class->user_id);

                PointsRecord::addRecord($class->username,$class->user_id,$current,$income,PointsRecord::INCOME,$class->id,$class->name,false);
                PointsRecord::addPoints($class->user_id,$income);

                PointsRecord::addRecord(self::GAM_NAME,self::GAM,$current,$tax,PointsRecord::TAX,$class->id,$class->name,false);
                PointsRecord::addPoints(self::GAM,$tax);
            }catch (\Exception $exception){
                echo $exception->getMessage();
                exit;
            }
            $bar->advance();
        }
        $bar->finish();
        echo PHP_EOL;
        exit;
    }
}
