#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    class GraphFileManager
    {
        public void SaveGraphDataToFile(string path,GraphChartBase graph)
        {
            IInternalGraphData data = (IInternalGraphData)graph;
            using (StreamWriter file = new StreamWriter(path))
            {
                foreach (var cat in data.Categories)
                {
                    file.WriteLine(cat.Name);
                    file.WriteLine(cat.Data.Count);
                    for (int i = 0; i < cat.Data.Count; i++)
                    {
                        DoubleVector3 item = cat.Data[i];
                        file.WriteLine(item.x);
                        file.WriteLine(item.y);
                    }
                }
            }
        }
        public void LoadGraphDataFromFile(string path, GraphChartBase graph)
        {
            try
            {
                using (StreamReader file = new StreamReader(path))
                {


                    while (file.Peek() > 0)
                    {
                        string catName = file.ReadLine();
                        if (graph.DataSource.HasCategory(catName) == false)
                            throw new Exception("category does not exist in the graph");
                        int count = int.Parse(file.ReadLine());
                        for(int i=0; i<count; i++)
                        {
                            double x = double.Parse(file.ReadLine());
                            double y = double.Parse(file.ReadLine());
                            graph.DataSource.AddPointToCategory(catName, x, y);
                        }
                    }
                }
            }
            catch(Exception)
            {
                throw new Exception("Invalid file format");
            }
        }
    }
}
