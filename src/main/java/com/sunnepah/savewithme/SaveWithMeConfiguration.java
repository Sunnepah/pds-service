package com.sunnepah.savewithme;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import com.meltmedia.dropwizard.mongo.MongoConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SaveWithMeConfiguration extends Configuration {
	
    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();
    
    @Valid
    @NotNull
    @JsonProperty
    private ClientSecretsConfiguration clientSecrets = new ClientSecretsConfiguration();

	@Valid
	@NotNull
	@JsonProperty
	private Oauth oauth;

	@JsonProperty("influxdb")
	private MetricsConfig metrics;

	@JsonProperty
    protected MongoConfiguration mongo;

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

	public JerseyClientConfiguration getJerseyClient() {
        return httpClient;
    }
	
	public ClientSecretsConfiguration getClientSecrets() {
		return clientSecrets;
	}

	public Oauth getOauth() {
		return oauth;
	}

	public void setOauth(Oauth oauth) {
		this.oauth = oauth;
	}

    public MongoConfiguration getMongo() {
        return mongo;
    }

	public MetricsConfig getMetrics() {
		return metrics;
	}

	public void setMetrics(MetricsConfig metrics) {
		this.metrics = metrics;
	}
	
	public static class ClientSecretsConfiguration {
		
		@NotBlank
		@JsonProperty
		String facebook;
		
		@NotBlank
		@JsonProperty
		String google;
		
		@NotBlank
		@JsonProperty
		String linkedin;
		
		@NotBlank
		@JsonProperty
		String github;
		
		@NotBlank
		@JsonProperty
		String foursquare;
		
		@NotBlank
		@JsonProperty
		String twitter;
		
		public String getFacebook() {
			return facebook;
		}

		public String getGoogle() {
			return google;
		}
		
		public String getLinkedin() {
			return linkedin;
		}
		
		public String getFoursquare() {
			return foursquare;
		}
		
		public String getTwitter() {
			return twitter;
		}
	}

	public static class Oauth {
		public Facebook facebook;

		public Google google;
	}

	public static class Facebook {
		@NotBlank
		@JsonProperty
		String accessTokenUrl;

		@NotBlank
		@JsonProperty
		String graphApiUrl;

		public String getAccessTokenUrl() {
			return accessTokenUrl;
		}

		public void setAccessTokenUrl(String accessTokenUrl) {
			this.accessTokenUrl = accessTokenUrl;
		}

		public String getGraphApiUrl() {
			return graphApiUrl;
		}

		public void setGraphApiUrl(String graphApiUrl) {
			this.graphApiUrl = graphApiUrl;
		}
	}

	public static class Google {
		@NotBlank
		@JsonProperty
		String accessTokenUrl;

		@NotBlank
		@JsonProperty
		String peopleApiUrl;

		public String getAccessTokenUrl() {
			return accessTokenUrl;
		}

		public void setAccessTokenUrl(String accessTokenUrl) {
			this.accessTokenUrl = accessTokenUrl;
		}

		public String getPeopleApiUrl() {
			return peopleApiUrl;
		}

		public void setPeopleApiUrl(String peopleApiUrl) {
			this.peopleApiUrl = peopleApiUrl;
		}
	}

	public static class MetricsConfig {
		@JsonProperty("server")
		private String server;

		@JsonProperty("port")
		private Integer port;

		@JsonProperty("prefix")
		private String prefix;

		public String getServer() {
			return server;
		}

		public void setServer(String server) {
			this.server = server;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
	}
}
