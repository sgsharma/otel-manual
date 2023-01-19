/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package hipstershop;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
// import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

class ExampleConfiguration {

  static OpenTelemetry initOpenTelemetry() {

    // Set to process the spans with the LoggingSpanExporter
    // LoggingSpanExporter exporter = LoggingSpanExporter.create();

    Resource resource = Resource.getDefault()
        .merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, System.getenv("OTEL_SERVICE_NAME"))));
  
    OtlpGrpcSpanExporter otlpExporter = OtlpGrpcSpanExporter.builder()
        .setEndpoint("https://api.honeycomb.io:443")
        .addHeader​("x-honeycomb-team", System.getenv("HONEYCOMB_API_KEY"))
        .build();

    SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
        .addSpanProcessor(BatchSpanProcessor.builder(otlpExporter).build())
        .setResource(resource)
        .build();

    OpenTelemetrySdk openTelemetrySdk =
        OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
            // install the W3C Trace Context propagator
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .build();

    // it's always a good idea to shutdown the SDK when your process exits.
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  System.err.println(
                      "*** forcing the Span Exporter to shutdown and process the remaining spans");
                  sdkTracerProvider.shutdown();
                  System.err.println("*** Trace Exporter shut down");
                }));

    return openTelemetrySdk;
  }
}
