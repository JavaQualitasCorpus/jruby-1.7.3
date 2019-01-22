require File.expand_path('../../../spec_helper', __FILE__)

ruby_version_is "1.9" do

  require File.expand_path('../../../shared/complex/conjugate', __FILE__)

  describe "Complex#conjugate" do
    it_behaves_like(:complex_conjugate, :conjugate)
  end
end
