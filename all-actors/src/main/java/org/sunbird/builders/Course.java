package org.sunbird.builders;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * this is a course class will be used to for a course object needs to be saved in ES.
 * @author anmolgupta
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {

    private String batchId;
    private String id;
    private String userId;
    private String completionUrl;
    private String introUrl;


    public Course() {
    }


    public String getBatchId() {
        return batchId;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getCompletionUrl() {
        return completionUrl;
    }

    public String getIntroUrl() {
        return introUrl;
    }

    @Override
    public String toString() {
        return "Course{" +
                "batchId='" + batchId + '\'' +
                ", id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", completionUrl='" + completionUrl + '\'' +
                ", introUrl='" + introUrl + '\'' +
                '}';
    }

    private Course(CourseBuilder courseBuilder) {
        this.batchId = courseBuilder.batchId;
        this.id =  courseBuilder.id;
        this.userId =  courseBuilder.userId;
        this.completionUrl =  courseBuilder.completionUrl;
        this.introUrl =  courseBuilder.introUrl;
    }

    public static class CourseBuilder{


        private String batchId;
        private String id;
        private String userId;
        private String completionUrl;
        private String introUrl;

        public CourseBuilder setBatchId(String batchId) {
            this.batchId = batchId;
            return this;
        }

        public CourseBuilder setId(String id) {
            this.id = id;
            return this;

        }

        public CourseBuilder setUserId(String userId) {
            this.userId = userId;
            return this;

        }

        public CourseBuilder setCompletionUrl(String completionUrl) {
            this.completionUrl = completionUrl;
            return this;

        }

        public CourseBuilder setIntroUrl(String introUrl) {
            this.introUrl = introUrl;
            return this;

        }
        public Course build(){
            Course course=new Course(this);
            return course;
        }
    }
}
