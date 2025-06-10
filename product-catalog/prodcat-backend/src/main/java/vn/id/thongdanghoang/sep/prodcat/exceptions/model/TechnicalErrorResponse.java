package vn.id.thongdanghoang.sep.prodcat.exceptions.model;


public record TechnicalErrorResponse(String correlationId, String message) implements
    ServerErrorResponse {

  @Override
  public ServerErrorType getErrorType() {
    return ServerErrorType.TECHNICAL;
  }
}
