#define Graph_And_Chart_PRO
using UnityEngine;
using ChartAndGraph;

/// <summary>
/// this tutorial follows the same pattern the graph tutorial goes. The only diffrence is that candle values are used.
/// the values are in the follwing order : open high low close , start time , time durtaion
/// </summary>
public class CandleChartFeed : MonoBehaviour
{
    float time = 3f;
    float x = 10f;

    void Start()
    {
        CandleChart candle = GetComponent<CandleChart>();
        if (candle != null)
        {
            candle.DataSource.StartBatch();
            candle.DataSource.ClearCategory("Player 1");
            for (int i = 0; i < 30; i++)
            {
                candle.DataSource.AddCandleToCategory("Player 1", new CandleChartData.CandleValue(Random.Range(5f, 10f), Random.Range(10f, 15f), Random.Range(0f, 5f), Random.Range(5f, 10f), i * 10f / 30f, Random.value * 10f / 30f));
            }

            candle.DataSource.EndBatch();
        }
    }

    private void Update()
    {
        time -= Time.deltaTime;
        if (time <= 0)
        {
            time = 3f;
            x += 0.3f;
            CandleChart candle = GetComponent<CandleChart>();
            candle.DataSource.AddCandleToCategory("Player 1", new CandleChartData.CandleValue(Random.Range(5f, 10f), Random.Range(10f, 15f), Random.Range(0f, 5f), Random.Range(5f, 10f), x, Random.value * 10f / 30f), 1f);

        }
    }
}
