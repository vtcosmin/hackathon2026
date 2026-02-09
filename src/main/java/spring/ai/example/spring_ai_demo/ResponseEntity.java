package spring.ai.example.spring_ai_demo;

class ResponseEntity {
    public String name;
    public String description;
    public String link;
    public String importantFindings;

    public String getImportantFindings() {
        return importantFindings;
    }

    public void getImportantFindings(String importantFindings) {
        this.importantFindings = importantFindings;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}