package vn.id.thongdanghoang.api.exceptions.model;


public record TechnicalErrorResponse(String correlationId, String message) implements
    ServerErrorResponse {

  @Override
  public ServerErrorType getErrorType() {
    return ServerErrorType.TECHNICAL;
  }
}
