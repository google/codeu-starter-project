google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart(){
  // Create a new DataTable instance
  var disney_data = new google.visualization.DataTable();

  // Define columns for the DataTable instance
  disney_data.addColumn('string', 'Disney Movie Title');
  disney_data.addColumn('number', 'Votes');

  // Fill the table with static example data
  // TODO: Load and update the data at runtime.
  disney_data.addRows([
      ["Sleeping Beauty", 6],
      ["Wreck it Ralph", 10],
      ["Peter Pan", 7],
      ["Big Hero Six", 15],
      ["The Lion King", 20]
  ]);

  // Add chart options
  var chart_options = {
      title: "Disney Movie Votes",
      width: 900,
      height: 400,
  };

  // Create new instance of barchart
  var chart = new google.visualization.BarChart(document.getElementById('disney_chart'));

  // Draw chart
  chart.draw(disney_data, chart_options);
}
