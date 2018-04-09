Backend setup:
	- LuceneBackend.jar should be at the same directory level as textDoc and util
	- put all the text-collection files in backend/textDoc
	- in terminal cd to where LuceneBackend.jar located, type: java -cp LuceneBackend.jar BasicIndexFiles
		- this will index all files under textDoc directory and store index at util/index unless otherwise specified
	- To start the server:
		- in terminal, type: java -cp LuceneBackend.jar BackendServer
		- by default, the server will be at port 9487.
Frontend setup:
	- in terminal cd to the directory where the LuceneFrontend.jar located, type: java -cp LuceneFrontend.jar Main
	- if frontend window close automatically it means it either encounter IOException caused by server (usually by wrong host address or port) or by other Exception. I used system.exit(0) for all exceptions... it may cause the application freeze when it encounters errors.

Discovered issue:
	- when enter “,”,(,)… in the query text-field (anything other than text, comma and period), it may cause backend server to terminate because of query-parsing issue.
	- spell-checking button doesn’t have assigned action yet (same for the 6 buttons for term association)
	- A new search can't not be triggered after using the "previous" button. Click "new feature" or "orig feature" instead for a new search.

Text Dataset:
	- Trec dataset(Ad_hoc Test collection). https://trec.nist.gov/data.html
	- textDoc folder only contains 2 sample files because the entire dataset is too big
