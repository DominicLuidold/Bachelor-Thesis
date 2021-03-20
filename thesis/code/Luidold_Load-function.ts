[...]

private loadSampleData() {
    this.sampleService.getSampleData().subscribe(data => {this.data = data});
}

[...]
