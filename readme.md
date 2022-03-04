# Search Engine

## setup 
### docker image
make sure you place this repository and https://github.com/jordanleeeee/EsFrontend
under the same directory

run build script
```bash
sh buildSearch.sh {{tagName}}
```

### setup data in es
1) execute `es.UpdateIndexStructure.main` to init index structure
2) execute `webcrawling.Main.main` to index document
3) execute `pagerank.Main.main` to add pagerank
