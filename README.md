To use:

Run java -jar [pathToRootDirectory]/out/artifacts/BroadInstituteOA_jar/BroadInstituteOA.jar. You will then be 
prompted with "Which question would you like to run?". Enter "1" to see output for question 1, "2" to see output for 
question 2, or "3 [firstStop] [secondStop]", where firstStop and secondStop are valid stop names, to see output for 
question 3. After output is printed, you will be prompted to enter another input. Enter "q" when you want to 
terminate the program.

To solve #1, I chose to download all results from the Routes API and filter locally. While it may have been more 
memory efficient to filter on the API and only download the filtered results, this solution offers a significantly 
more robust design that can be easily extended upon for new problems in the future. By storing all of the routes as 
objects, we have the ability to perform more complicated queries on the objects based on the specific fields of the 
Routes. While this may be possible through API requests, it is far easier in the long-term to have classes built out 
then trying to create new API requests (which will also yield new JSON that we need to parse) each time we 
want to add new features to this tool. So, while there may be mild performance or memory loss as compared to a 
solution that filters using the API, this solution is preferable since it will be easier to extend this program in 
the future as compared to a solution that attempts to filter through the API.